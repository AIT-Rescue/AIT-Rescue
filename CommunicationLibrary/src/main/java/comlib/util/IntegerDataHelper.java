package comlib.util;

import java.util.HashMap;
import java.util.Map;

public class IntegerDataHelper
{
    private Map<String, int[]> dataMap;

    private int useBitFlag;

    private Boolean canRegister;

    //instance////////////////////////////////////////////////////////////////////////////

    public IntegerDataHelper() {
        this.dataMap = new HashMap<>();
        this.useBitFlag = 0;
        this.canRegister = Boolean.TRUE;
    }

    public IntegerDataHelper(int initialCapacity) {
        this.dataMap = new HashMap<>(initialCapacity);
        this.useBitFlag = 0;
        this.canRegister = Boolean.TRUE;
    }

    public IntegerDataHelper(int initialCapacity, float loadFactor) {
        this.dataMap = new HashMap<>(initialCapacity, loadFactor);
        this.useBitFlag = 0;
        this.canRegister = Boolean.TRUE;
    }

    //method//////////////////////////////////////////////////////////////////////////////

    public boolean registerData(String name, int start, int size) {
        if(this.exist(name))
            return false;
        if(!this.canRegisterData(start, size))
            return false;

        int filter = getFilter(start, size);
        this.dataMap.put(name, new int[]{start, filter});
        this.setUseBit(filter);
        return true;
    }

    public boolean registerFlag(String name, int start) {
        return this.registerData(name, start, 1);
    }

    public int setData(int data, String name, int input) {
        int[] datas = this.dataMap.get(name);
        if(datas == null) return data;
        return setByFilter(data, datas[0], datas[1], input);
    }

    public int setFlag(int data, String name, boolean input) {
        return input ? this.setData(data, name, 1) : this.setData(data, name, 0);
    }

    public Integer getData(int data, String name) {
        int[] datas = this.dataMap.get(name);
        if(datas == null) return null;
        return getByFilter(data, datas[0], datas[1]);
    }

    public Boolean getFlag(int data, String name) {
        Integer flag = this.getData(data, name);
        if(flag == null) return null;
        return flag != 0;
    }

    public boolean canRegisterData(int start, int size) {
        return this.canRegister && !(start < 0 || size <= 0 || (start + size - 1) > 31) && ((this.useBitFlag & getFilter(start, size)) == 0);
    }

    private void setUseBit(int filter) {
        this.useBitFlag = this.useBitFlag | filter;
        if(this.useBitFlag == 0xFFFFFFFF)
            this.canRegister = Boolean.FALSE;
    }

    public boolean exist(String name) {
        return this.dataMap.get(name) != null;
    }

    //static method///////////////////////////////////////////////////////////////////////

    public static int getByFilter(int data, int start, int filter) {
        return (data & filter) >> start;
    }

    public static int get(int data, int start, int size) {
        return (data >> start) & ((0x01 << size) - 1);
    }

    public static int setByFilter(int data, int start, int filter, int input) {
        input = (input << start) & filter;
        data = data & ~filter;
        return data | input;
    }

    public static int set(int data, int start, int size, int input) {
        return setByFilter(data, start, getFilter(start, size), input);
    }

    public static int removeByFilter(int data, int filter) {
        return data & ~filter;
    }

    public static int remove(int data, int start, int size) {
        return removeByFilter(data, getFilter(start, size));
    }

    public static int getFilter(int start, int size) {
        return ((0x01 << size) - 1) << start;
    }

    /*public static int getBitSize(int value) {
        int i = (value >> 16) != 0 ? (value >> 24) != 0 ? 31 : 23 : (value & 0xFF00) != 0 ? 15 : 7;
        do {
            if (((value >> i) & 0x01) == 1)
                return i + 1;
            i--;
        } while (i > 0);
        return 1;
    }*/

    /*public static int getBitSize(int value) {
        int i;
        if((value >> 16) != 0)
            i = (value >> 24) != 0 ? (value >> 28) != 0 ? 31 : 27 : (value >> 0x00F00000) != 0 ? 23 : 19;
        else
            i = (value & 0xFF00) != 0 ? (value & 0xF000) != 0 ? 15 : 11 : (value >> 0xF0) != 0 ? 7 : 3;
        do {
            if (((value >> i) & 0x01) == 1)
                return i + 1;
            i--;
        } while (i > 0);
        return 1;
    }*/

    //TODO: どちらが速いか
    public static int getBitSizeOld(int value) {
        for(int i = (value >> 16) != 0 ? 31 : 15; i > 0; i--)
            if(((value >> i) & 0x01) == 1)
                return i + 1;
        return 1;
    }

    public static int getBitSize(int value) {
        if((value & 0xFFFF0000) != 0) {
            if ((value & 0xFF000000) != 0) {
                if ((value & 0xF0000000) != 0) {
                    return (value & 0xC0000000) != 0 ? (value & 0x80000000) != 0 ? 32 : 31 : (value & 0x20000000) != 0 ? 30 : 29;
                } else {
                    return (value & 0x0C000000) != 0 ? (value & 0x08000000) != 0 ? 28 : 27 : (value & 0x02000000) != 0 ? 26 : 25;
                }
            }
            else {
                if ((value & 0x00F00000) != 0) {
                    return (value & 0x00C00000) != 0 ? (value & 0x00800000) != 0 ? 24 : 23 : (value & 0x00200000) != 0 ? 22 : 21;
                } else {
                    return (value & 0x000C0000) == 0 ? (value & 0x00020000) != 0 ? 18 : 17 : (value & 0x00080000) != 0 ? 20 : 19;
                }
            }
        }
        else { //0x0000FFFF
            if ((value & 0x0000FF00) != 0) {
                if ((value & 0x0000F000) != 0) {
                    return (value & 0x0000C000) != 0 ? (value & 0x00008000) != 0 ? 16 : 15 : (value & 0x00002000) != 0 ? 14 : 13;
                } else { //0x0F000000
                    return (value & 0x00000C00) != 0 ? (value & 0x00000800) != 0 ? 12 : 11 : (value & 0x00000200) != 0 ? 10 : 9;
                }
            }
            else { //00FF0000
                if ((value & 0x000000F0) != 0) {
                    return (value & 0x000000C0) != 0 ? (value & 0x00000080) != 0 ? 8 : 7 : (value & 0x00000020) != 0 ? 6 : 5;
                } else { //0x000F0000
                    return (value & 0x0000000C) != 0 ? (value & 0x00000008) != 0 ? 4 : 3 : (value & 0x00000002) != 0 ? 2 : 1;
                }
            }
        }
    }
}