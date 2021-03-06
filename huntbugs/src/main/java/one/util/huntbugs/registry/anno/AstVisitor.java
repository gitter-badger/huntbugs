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
package one.util.huntbugs.registry.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import one.util.huntbugs.registry.MethodContext;

import com.strobel.assembler.metadata.MethodDefinition;
import com.strobel.assembler.metadata.TypeDefinition;

/**
 * Method in detector class which called for AST nodes.
 * 
 * <p>
 * Allowed parameter types (no repeats): {@link MethodContext},
 * {@link MethodDefinition}, {@link TypeDefinition} or any registered databases
 * (see {@link TypeDatabase}, {@link TypeDatabaseItem})
 * 
 * <p>
 * For additional allowed types and allowed return values see the {@link AstNodes} description.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AstVisitor {
    AstNodes nodes() default AstNodes.ALL;
    
    String methodName() default "";

    String methodSignature() default "";
}
