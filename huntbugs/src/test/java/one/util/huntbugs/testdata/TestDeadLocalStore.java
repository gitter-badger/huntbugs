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
public class TestDeadLocalStore {
    int x;
    
    @AssertWarning(type="ParameterOverwritten", minScore=55)
    public void testDeadLocalSimple(int x) {
        x = 10;
        System.out.println(x);
    }

    @AssertNoWarning(type="ParameterOverwritten")
    public void testDeadLocalBranch(int x) {
        if(Math.random() > 0.5)
            x = 10;
        System.out.println(x);
    }
    
    class Extension extends TestDeadLocalStore {
        @Override
        @AssertWarning(type="ParameterOverwritten", maxScore = 40)
        public void testDeadLocalSimple(int x) {
            x = 10;
            System.out.println(x);
        }
    }

    @AssertWarning(type="DeadIncrementInReturn")
    public int testDeadIncrement(int x) {
        return x++;
    }
    
    @AssertNoWarning(type="DeadIncrementInReturn")
    public int testFieldIncrement() {
        return x++;
    }
    
    @SuppressWarnings("unused")
    @AssertWarning(type="DeadStoreInReturn")
    public boolean testDeadStore(boolean b) {
        return b = true;
    }
}
