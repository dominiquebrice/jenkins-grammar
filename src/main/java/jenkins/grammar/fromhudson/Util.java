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

public class Util {
  /* ------------------------------------------------------------ */
  /** Unquote a string.
   * @param s The string to unquote.
   * @return quoted string
   */
  public static String unquote(String s)
  {
      if (s==null) {
        return null;
      }
      if (s.length()<2) {
        return s;
      }

      char first=s.charAt(0);
      char last=s.charAt(s.length()-1);
      if (first!=last || (first!='"' && first!='\'')) {
        return s;
      }

      StringBuilder b=new StringBuilder(s.length()-2);
      boolean escape=false;
      for (int i=1;i<s.length()-1;i++)
      {
          char c = s.charAt(i);

          if (escape)
          {
              escape=false;
              switch (c)
              {
                  case 'n':
                      b.append('\n');
                      break;
                  case 'r':
                      b.append('\r');
                      break;
                  case 't':
                      b.append('\t');
                      break;
                  case 'f':
                      b.append('\f');
                      break;
                  case 'b':
                      b.append('\b');
                      break;
                  case 'u':
                      b.append((char)(
                              (convertHexDigit((byte)s.charAt(i++))<<24)+
                              (convertHexDigit((byte)s.charAt(i++))<<16)+
                              (convertHexDigit((byte)s.charAt(i++))<<8)+
                              (convertHexDigit((byte)s.charAt(i++)))
                              )
                      );
                      break;
                  default:
                      b.append(c);
              }
          }
          else if (c=='\\')
          {
              escape=true;
              continue;
          }
          else {
            b.append(c);
          }
      }

      return b.toString();
  }
  
  /**
   * @param b An ASCII encoded character 0-9 a-f A-F
   * @return The byte value of the character 0-16.
   */
  public static byte convertHexDigit( byte b )
  {
      if ((b >= '0') && (b <= '9')) {
        return (byte)(b - '0');
      }
      if ((b >= 'a') && (b <= 'f')) {
        return (byte)(b - 'a' + 10);
      }
      if ((b >= 'A') && (b <= 'F')) {
        return (byte)(b - 'A' + 10);
      }
      return 0;
  }
}
