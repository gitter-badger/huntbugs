/*
 * Copyright 2015, 2016 Tagir Valeev
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.util.huntbugs.analysis;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.strobel.assembler.metadata.ClasspathTypeLoader;
import com.strobel.assembler.metadata.CompositeTypeLoader;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.TypeDefinition;

import one.util.huntbugs.registry.DetectorRegistry;
import one.util.huntbugs.repo.Repository;
import one.util.huntbugs.repo.RepositoryVisitor;
import one.util.huntbugs.warning.Warning;

/**
 * @author lan
 *
 */
public class Context {
    private final List<ErrorMessage> errors = Collections.synchronizedList(new ArrayList<>());
    private final List<Warning> warnings = Collections.synchronizedList(new ArrayList<>());
    private final DetectorRegistry registry;
    private final MetadataSystem ms;
    private final Repository repository;
    private final AtomicInteger classesCount = new AtomicInteger();
    private int totalClasses = 0;
    private final AnalysisOptions options;
    private final List<AnalysisListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<String, Long> stat = new ConcurrentHashMap<>();

    public Context(Repository repository, AnalysisOptions options) {
        registry = new DetectorRegistry(this);
        this.repository = repository;
        this.options = options;
        ITypeLoader loader = repository.createTypeLoader();
        if (options.addBootClassPath) {
            loader = new CompositeTypeLoader(new ClasspathTypeLoader(System.getProperty("sun.boot.class.path")), loader);
        }
        ms = new MetadataSystem(loader);
    }

    public AnalysisOptions getOptions() {
        return options;
    }
    
    public void addListener(AnalysisListener listener) {
        listeners.add(listener);
    }
    
    boolean fireEvent(String stepName, String className) {
        for(AnalysisListener listener : listeners) {
            if(!listener.eventOccurred(stepName, className))
                return false;
        }
        return true;
    }

    public void analyzePackage(String name) {
        if(!fireEvent("Gathering statistics", null))
            return;
        List<String> classes = new ArrayList<>();
        repository.visit(name, new RepositoryVisitor() {

            @Override
            public boolean visitPackage(String packageName) {
                return true;
            }

            @Override
            public void visitClass(String className) {
                classes.add(className);
            }
        });
        totalClasses = classes.size();
        for(String className : classes) {
            if(!fireEvent("Analyzing class", className))
                return;
            analyzeClass(className);
        }
    }

    public void analyzeClass(String name) {
        classesCount.incrementAndGet();
        TypeDefinition type;
        try {
            type = ms.resolve(ms.lookupType(name));
        } catch (Throwable t) {
            addError(new ErrorMessage(null, name, null, null, -1, t));
            return;
        }
        if (type != null)
            registry.analyzeClass(type);
    }

    public void addError(ErrorMessage msg) {
        errors.add(msg);
    }

    public void addWarning(Warning warning) {
        warnings.add(warning);
    }

    public void reportWarnings(PrintStream app) {
        List<Warning> warns = new ArrayList<>(warnings);
        warns.sort(Comparator.comparingInt(Warning::getScore).reversed().thenComparing(w -> w.getType().getName()));
        warns.forEach(w -> app.append(w.toString()).append("\n"));
    }
    
    public void reportStats(PrintStream app) {
        if(stat.isEmpty())
            return;
        app.append("Statistics:\n");
		stat.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.forEach(
						e -> app.append("\t").append(e.getKey()).append(" = ")
								.append(e.getValue().toString()).append("\n"));
    }

    public void reportErrors(PrintStream app) {
        errors.forEach(msg -> app.append(msg.toString()).append("\n"));
    }

    public int getClassesCount() {
        return classesCount.get();
    }

    public int getTotalClasses() {
        return totalClasses;
    }
    
    public int getErrorCount() {
        return errors.size();
    }

    public int getWarningCount() {
        return warnings.size();
    }

	public void incStat(String key) {
	    stat.merge(key, 1L, Long::sum);
	}
}
