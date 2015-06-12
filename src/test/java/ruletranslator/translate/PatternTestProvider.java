package ruletranslator.translate;

import static junitparams.JUnitParamsRunner.*;
import junitparams.JUnitParamsRunner;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PatternTestProvider{

    public Object[] parametersBodyPattern() {
        return $(
                $("I_MYITEM", true),
                $("IG_MYGROUP", true),
                $("  SOMESTUFF I_MYITEM", true),
                $("  SOMESTUFF I_MYITEM MORESTUFF", true),
                $("NOTHING HERE", false),
                $("IG _MYGROUP", false),
                $("F_NOMATCH", false)
        );
    }

    public Object[] parametersHeaderPattern() {
        return $(
                $("   SOME_NAME I_SOMEID ", true),
                $("   SOME_NAME IG_MYGROUP", true),
                $("   SOME_NAME IG_MYGROUP TOO_MUCH_INFO", false),
                $("   I_SOMEID", false),
                $("I_SOMEID", false),
                $("NOSPACE I_SOMEID", false)
        );
    }

}
