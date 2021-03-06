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

import java.util.ArrayList;
import java.util.List;

import one.util.huntbugs.registry.anno.AssertNoWarning;
import one.util.huntbugs.registry.anno.AssertWarning;

/**
 * @author lan
 *
 */
public class TestNumericComparison {
    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testChar(char c) {
        if(c < 0) {
            System.out.println("Never!");
        }
        if(c >= 0) {
            System.out.println("Always");
        }
    }

    @AssertNoWarning(type="ComparisonWithOutOfRangeValue")
    public void testCharOk(char c) {
        int r = c - 'a';
        if(r < 0) {
            System.out.println("Ok!");
        }
    }

    @AssertNoWarning(type="*")
    public void testAssert(char c) {
        assert c >= 0;
        if(c == 'x') {
            System.out.println("X!");
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testByte(Byte b) {
        int i = b.byteValue();
        if(i < 0x80) {
            System.out.println("Always");
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testInt(int c) {
        if(c < 0x100000000000L) {
            System.out.println("Always!");
        }
    }
    
    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testArrayLength(int[] array) {
        if(array.length < 0) {
            System.out.println("Never!");
        }
    }

    @AssertWarning(type="SwitchBranchUnreachable")
    public int testArrayLengthSwitch(int[] array) {
        switch(array.length) {
        case 0:
            return -1;
        case 1:
            return 0;
        case 2:
            return 10;
        case Integer.MAX_VALUE:
            return 12;
        case -1:
            return Integer.MIN_VALUE;
        default:
            return -2;
        }
    }

    @AssertNoWarning(type="SwitchBranchUnreachable")
    public int testArrayLengthSwitchOk(int[] array) {
        switch(array.length) {
        case 0:
            return -1;
        case 1:
            return 0;
        case 2:
            return 10;
        case Integer.MAX_VALUE:
            return 12;
        default:
            return -2;
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testListLength(ArrayList<String> list) {
        if(list.size() == -1) {
            System.out.println("Never!");
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testBitOp(int input) {
        int result = input & 0xFF0;
        if(result > 0xFFFF) {
            System.out.println("Never!");
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testRem(int input) {
        int result = input % 3;
        if(result == 3) {
            System.out.println("Never!");
        }
    }

    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testRem(List<String> list) {
        int result = list.size() % 3;
        if(result < 0) {
            System.out.println("Never!");
        }
    }

    @AssertNoWarning(type="ComparisonWithOutOfRangeValue")
    public void testShrOk(int input) {
        int result = input >> 10;
        if(result == 0x1FFFFF || result == -0x200000) {
            System.out.println("Ok");
        }
    }
    
    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testShr(int input) {
        int result = input >> 10;
        if(result == 0x200000) {
            System.out.println("Never");
        }
    }
    
    @AssertNoWarning(type="ComparisonWithOutOfRangeValue")
    public void testUShrOk(int input) {
        int result = input >>> 10;
        if(result == 0x3FFFFF) {
            System.out.println("Ok");
        }
    }
    
    @AssertWarning(type="ComparisonWithOutOfRangeValue")
    public void testUShr(int input) {
        int result = input >>> 10;
        if(result == 0x400000) {
            System.out.println("Never");
        }
    }

    @AssertWarning(type="CheckForOddnessFailsForNegative")
    public void testRem2(int input) {
        if(input % 2 == 1) {
            System.out.println("odd");
        }
    }

    @AssertNoWarning(type="CheckForOddnessFailsForNegative")
    public void testRem2Ok(List<String> list) {
        if(list.size() % 2 == 1) {
            System.out.println("odd");
        }
    }
}
