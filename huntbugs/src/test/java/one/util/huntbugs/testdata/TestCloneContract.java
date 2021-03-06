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
package one.util.huntbugs.testdata;

import one.util.huntbugs.registry.anno.AssertNoWarning;
import one.util.huntbugs.registry.anno.AssertWarning;

/**
 * @author lan
 *
 */
public class TestCloneContract {
    @AssertWarning(type="CloneableDoesNotImplementClone")
    public class Clone0 implements Cloneable {
        
    }
    
    public class Clone1 implements Cloneable {
        public int f = 0;
        
        @Override
        @AssertWarning(type="CloneableNoSuperCall", minScore=55)
        protected Object clone() throws CloneNotSupportedException {
            return new Clone1();
        }
    }

    public class Clone2 extends Clone1 {
        @Override
        @AssertWarning(type="CloneableNoSuperCall", minScore=45, maxScore=54)
        protected Object clone() throws CloneNotSupportedException {
            return new Clone2();
        }
    }
    
    @Override
    @AssertWarning(type="NotCloneableHasClone")
    public TestCloneContract clone() {
        return new TestCloneContract();
    }
    
    public class NotCloneable {
        @Override
        @AssertNoWarning(type="*")
        public Object clone() {
            throw new UnsupportedOperationException();
        }
    }
}
