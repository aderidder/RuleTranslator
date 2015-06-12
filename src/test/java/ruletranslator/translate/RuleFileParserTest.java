package ruletranslator.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class RuleFileParserTest {

    private static String getPrivateStaticVariable(Class targetClass, String variableName)throws InvocationTargetException {
        try{
            Field field = targetClass.getDeclaredField(variableName);
            field.setAccessible(true);
            // static so get with null as argument
            return String.valueOf(field.get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InvocationTargetException(e.getCause(), e.getClass().toString()+": "+e.getMessage());
        }
    }

    @Test
    @Parameters(source = PatternTestProvider.class, method = "parametersBodyPattern")
    public void testBodyPattern(String input, Boolean expectedToMatch) throws InvocationTargetException {
        String bodyExpression = getPrivateStaticVariable(RuleFileParser.class, "bodyPattern");
        Pattern bodyPattern = Pattern.compile(bodyExpression);

        // Given a matcher on bodyPattern from the constructor input
        Matcher matcher = bodyPattern.matcher(input);

        // Expect matcher.matches should return what's expected
        assertThat(matcher.matches(), is(expectedToMatch));
    }


    @Test
    @Parameters(source = PatternTestProvider.class, method = "parametersHeaderPattern")
    public void testHeaderPattern(String input, Boolean expectedToMatch) throws InvocationTargetException {
        String bodyExpression = getPrivateStaticVariable(RuleFileParser.class, "headerPattern");
        Pattern bodyPattern = Pattern.compile(bodyExpression);

        // Given a matcher on bodyPattern from the constructor input
        Matcher matcher = bodyPattern.matcher(input);

        // Expect matcher.matches should return what's expected
        assertThat(matcher.matches(), is(expectedToMatch));
    }

}

