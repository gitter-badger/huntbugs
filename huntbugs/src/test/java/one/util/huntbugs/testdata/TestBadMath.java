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
public class TestBadMath {
    @AssertWarning(type="RemOne")
    public int testRem(int x) {
        int mod = 1;
        int add = 0;
        if (x == 2)
            mod += add;
        return x % mod;
    }

    @AssertWarning(type="RemOne")
    public int testRemAbs(int x) {
        int mod = Math.abs(-1);
        return x % mod;
    }
    
    @AssertNoWarning(type="RemOne")
    public int testRemOk(int x) {
        int mod = 1;
        if (x == 2)
            mod = 2;
        return x % mod;
    }

    @AssertWarning(type="UselessOrWithZero")
    public int testOrZero(int x) {
        int arg = 0;
        return x | arg;
    }
    
    @AssertWarning(type="UselessOrWithZero")
    public int testXorZero(int x) {
        int arg = 0;
        return x ^ arg;
    }
    
    @AssertWarning(type="UselessAndWithMinusOne")
    public int testAndFFFF(int x) {
        return x & 0xFFFFFFFF;
    }
    
    @AssertNoWarning(type="UselessAndWithMinusOne")
    public long testAndFFFF(long x) {
        return x & 0xFFFFFFFFL;
    }
    
    @AssertWarning(type="UselessAndWithMinusOne")
    public long testAndFFFFIncorrect(long x) {
        return x & 0xFFFFFFFF;
    }
    
    @AssertWarning(type="UselessAndWithMinusOne")
    public long testAndFFFFConvert(long x) {
        int mask = 0xFFFFFFFF;
        return x & mask;
    }
}