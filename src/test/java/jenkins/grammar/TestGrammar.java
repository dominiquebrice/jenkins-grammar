package jenkins.grammar;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import jenkins.grammar.fromhudson.Label;
import jenkins.grammar.fromhudson.LabelAtom;
import jenkins.grammar.fromhudson.LabelExpression;

public class TestGrammar {

    @Test
    public void testAND() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader("mac-os && jdk17"));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.And);
        LabelExpression.And and = (LabelExpression.And) label;
        Assert.assertTrue(and.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", and.lhs.getDisplayName());
        Assert.assertTrue(and.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk17", and.rhs.getDisplayName());
        
        lexer = new LabelExpressionLexer(new StringReader("mac-os&&jdk17"));
        label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.And);
        and = (LabelExpression.And) label;
        Assert.assertTrue(and.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", and.lhs.getDisplayName());
        Assert.assertTrue(and.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk17", and.rhs.getDisplayName());
    }
    
    @Test
    public void testOR_NOT() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader("macos || !jdk-17"));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Or);
        LabelExpression.Or or = (LabelExpression.Or) label;
        Assert.assertTrue(or.lhs instanceof LabelAtom);
        Assert.assertEquals("macos", or.lhs.getDisplayName());
        Assert.assertTrue(or.rhs instanceof LabelExpression.Not);
        LabelExpression.Not not = (LabelExpression.Not)or.rhs;
        Assert.assertTrue(not.base instanceof LabelAtom);
        Assert.assertEquals("jdk-17", not.base.getDisplayName());
        
        lexer = new LabelExpressionLexer(new StringReader("macos||!jdk-17"));
        label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Or);
        or = (LabelExpression.Or) label;
        Assert.assertTrue(or.lhs instanceof LabelAtom);
        Assert.assertEquals("macos", or.lhs.getDisplayName());
        Assert.assertTrue(or.rhs instanceof LabelExpression.Not);
        not = (LabelExpression.Not)or.rhs;
        Assert.assertTrue(not.base instanceof LabelAtom);
        Assert.assertEquals("jdk-17", not.base.getDisplayName());
    }
    
    @Test
    public void testIFF() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader("macos <-> jdk17"));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Iff);
        LabelExpression.Iff iff = (LabelExpression.Iff) label;
        Assert.assertTrue(iff.lhs instanceof LabelAtom);
        Assert.assertEquals("macos", iff.lhs.getDisplayName());
        Assert.assertTrue(iff.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk17", iff.rhs.getDisplayName());
        
        lexer = new LabelExpressionLexer(new StringReader("macos<->jdk17"));
        label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Iff);
        iff = (LabelExpression.Iff) label;
        Assert.assertTrue(iff.lhs instanceof LabelAtom);
        Assert.assertEquals("macos", iff.lhs.getDisplayName());
        Assert.assertTrue(iff.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk17", iff.rhs.getDisplayName());
    }

    @Test
    public void testPAREN() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader("((a1 && a2) || a3)"));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Paren);
        LabelExpression.Paren p = (LabelExpression.Paren)label;
        Assert.assertTrue(p.base instanceof LabelExpression.Or);
        LabelExpression.Or or = (LabelExpression.Or)p.base;
        Assert.assertTrue(or.rhs instanceof LabelAtom);
        Assert.assertEquals(or.rhs.getDisplayName(), "a3");
        Assert.assertTrue(or.lhs instanceof LabelExpression.Paren);
        p = (LabelExpression.Paren)or.lhs;
        Assert.assertTrue(p.base instanceof LabelExpression.And);
        LabelExpression.And and = (LabelExpression.And)p.base;
        Assert.assertTrue(and.lhs instanceof LabelAtom);
        Assert.assertEquals("a1", and.lhs.getDisplayName());
        Assert.assertTrue(and.rhs instanceof LabelAtom);
        Assert.assertEquals("a2", and.rhs.getDisplayName());
    }
    
    @Test
    public void testIMPLIES_OK() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader("mac-os -> jdk-17"));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Implies);
        LabelExpression.Implies implies = (LabelExpression.Implies) label;
        Assert.assertTrue(implies.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", implies.lhs.getDisplayName());
        Assert.assertTrue(implies.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk-17", implies.rhs.getDisplayName());
        
        lexer = new LabelExpressionLexer(new StringReader("mac-os ->jdk-17"));
        label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Implies);
        implies = (LabelExpression.Implies) label;
        Assert.assertTrue(implies.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", implies.lhs.getDisplayName());
        Assert.assertTrue(implies.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk-17", implies.rhs.getDisplayName());
    }
    
    private static final String FAILING_LABEL = "mac-os-> jdk-17";
    
    @Test
    public void testIMPLIES_KO() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader(FAILING_LABEL));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Implies);
        LabelExpression.Implies implies = (LabelExpression.Implies) label;
        Assert.assertTrue(implies.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", implies.lhs.getDisplayName());
        Assert.assertTrue(implies.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk-17", implies.rhs.getDisplayName());
    }
    
    @Test
    public void testIMPLIES_KO_alt() throws Exception {
        LabelExpressionLexer lexer = new LabelExpressionLexer(new StringReader(prepareLabelForParsing(FAILING_LABEL)));
        Label label = new LabelExpressionParser(lexer).expr();
        Assert.assertTrue(label instanceof LabelExpression.Implies);
        LabelExpression.Implies implies = (LabelExpression.Implies) label;
        Assert.assertTrue(implies.lhs instanceof LabelAtom);
        Assert.assertEquals("mac-os", implies.lhs.getDisplayName());
        Assert.assertTrue(implies.rhs instanceof LabelAtom);
        Assert.assertEquals("jdk-17", implies.rhs.getDisplayName());
        
        prepareLabelForParsing(null);
    }
    
    private static final Pattern P = Pattern.compile(".*([^ ]->).*");
    
    private static String prepareLabelForParsing(String s) {
       if (s != null) {
            // if s is not null
            Matcher m = P.matcher(s);
            // look for '->' that are not preceded by a space character
            while (m.matches()) {
                // found at least one
                String unspacedImplies = m.group(1);
                // insert a space character between the previous token and '->'
                s = s.replace(unspacedImplies, unspacedImplies.charAt(0) + " ->");
                // and see if there's another one
                m = P.matcher(s);
            }
        }
        return s;
    }
}
