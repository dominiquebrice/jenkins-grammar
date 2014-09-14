/*
 * The MIT License
 *
 * Copyright (c) 2010, InfraDNA, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jenkins.grammar.fromhudson;

public abstract class Label {

  protected final String name;
  
  public Label(String name) {
    this.name = name;
  }
  
  public String getLabel() {
    return name;
  }
  
  /**
   * Returns the label that represents "this&lt;->rhs"
   */
  public Label iff(Label rhs) {
      return new LabelExpression.Iff(this,rhs);
  }
  
  /**
   * Returns the label that represents "this->rhs"
   */
  public Label implies(Label rhs) {
      return new LabelExpression.Implies(this,rhs);
  }
  
  /**
   * Returns the label that represents "this&amp;rhs"
   */
  public Label and(Label rhs) {
      return new LabelExpression.And(this,rhs);
  }
  
  /**
   * Returns the label that represents "(this)"
   * This is a pointless operation for machines, but useful
   * for humans who find the additional parenthesis often useful
   */
  public Label paren() {
      return new LabelExpression.Paren(this);
  }

  /**
   * Returns the label that represents "this|rhs"
   */
  public Label or(Label rhs) {
      return new LabelExpression.Or(this,rhs);
  }
  
  /**
   * Returns the label that represents "!this"
   */
  public Label not() {
      return new LabelExpression.Not(this);
  }
  
  /**
   * Returns a human-readable text that represents this label.
   */
  public String getDisplayName() {
      return name;
  }
  
  public abstract boolean matches(VariableResolver<Boolean> resolver);
  
  /**
   * Accepts a visitor and call its respective "onXYZ" method based no the actual type of 'this'.
   */
  public abstract <V,P> V accept(LabelVisitor<V,P> visitor, P param);
  
  /**
   * Precedence of the top most operator.
   */
  public abstract LabelOperatorPrecedence precedence();
  
  /**
   * Returns a label expression that represents this label.
   */
  public abstract String getExpression();
}
