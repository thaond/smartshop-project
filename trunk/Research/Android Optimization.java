1. For
    public void zero() {
        int sum = 0;
        for (int i = 0; i < mArray.length; ++i) {
            sum += mArray[i].mSplat;
        }
    }

    public void one() {
        int sum = 0;
        Foo[] localArray = mArray;
        int len = localArray.length;

        for (int i = 0; i < len; ++i) {
            sum += localArray[i].mSplat;
        }
    }

    public void two() {
        int sum = 0;
        for (Foo a : mArray) {
            sum += a.mSplat;
        }
    }
	
Two > One > Zero

2. Prefer static final
3. Perfer static method
4. Avoid getter and setter
5. Use exact type when creating new object 
ArrayList<String> a = new ArrayList<String>() > List<String a = new ArrayList<String>()
6. Avoid Creating Objects -> Try to reuse
7. Avoid use Collection, use native array
8. Avoid Enums Where You Only Need Ints
public enum Shrubbery { GROUND, CRAWLING, HANGING }
Use public static final int instead
9. Use Package Scope with Inner Classes
public class Foo {
    private int mValue;

    public void run() {
        Inner in = new Inner();
        mValue = 27;
        in.stuff();
    }

    private void doStuff(int value) {
        System.out.println("Value is " + value);
    }

    private class Inner {
        void stuff() {
            Foo.this.doStuff(Foo.this.mValue);
        }
    }
}

Change mValue modifier in Foo to public
10. Use Floating-Point Carefully
Use double instead of float
Reduce divide and modulo
11. Know And Use The Libraries
Use System.arraycopy, String.indexOf

References
http://www.jpct.net/wiki/index.php/Performance_tips_for_Android
http://d.android.com/guide/practices/design/performance.html