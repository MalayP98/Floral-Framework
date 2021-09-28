package com.dummyframework.deserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Separator {

    private final char ARRAY_START = '[';
    private final char ARRAY_END = ']';
    private final char PAYLOAD_START = '(';
    private final char PAYLOAD_END = ')';
    private final char STRING_START = '"';
    private final String COMMA_SEPRATOR = ",\\s?";

    private boolean verify(int ascii) {
        return ((ascii >= 48 && ascii <= 57) || (ascii >= 97 && ascii <= 122) || (ascii >= 65 && ascii <= 90)
                || ascii == 46);
    }

    public Map<String, String> toMap(String content) {
        Map<String, String> map = new HashMap<>();
        int flag = 0;
        String[] pair = new String[2];
        char close_char = ' ';
        for (int i = 0; i < content.length(); i++) {
            char start_char = content.charAt(i);
            if (start_char == ARRAY_START || start_char == PAYLOAD_START || start_char == STRING_START) {
                if (start_char == ARRAY_START) {
                    close_char = ARRAY_END;
                } else if (start_char == PAYLOAD_START) {
                    close_char = PAYLOAD_END;
                } else if (start_char == '\"') {
                    close_char = '\"';
                }
                int end_idx = extractTillClosingBracket(i, close_char, start_char, content);
                pair[flag] = content.substring(i, end_idx);
                i = end_idx - 1;
            } else if (start_char == '=') {
                if (flag == 1) {
                    flag = 0;
                } else {
                    flag = 1;
                }
            } else if (start_char == ',') {
                if (!pair[0].isEmpty() && !pair[1].isEmpty()) {
                    map.put(pair[0], pair[1]);
                    pair[0] = "";
                    pair[1] = "";
                    flag = 0;
                }
            } else if (start_char == ' ') {
                continue;
            } else {
                for (int j = i; j < content.length(); j++) {
                    if (j < content.length() - 1 && !verify(content.charAt(j))) {
                        pair[flag] = content.substring(i, j);
                        i = j - 1;
                        break;
                    } else if (j == content.length() - 1 && verify(content.charAt(j))) {
                        pair[flag] = content.substring(i, j + 1);
                    }
                }
            }
        }
        if (!pair[0].isEmpty() && !pair[1].isEmpty()) {
            map.put(pair[0], pair[1]);
            pair[0] = "";
            pair[1] = "";
            flag = 0;
        }
        return map;
    }

    public String[] toArray(String content) {
        List<String> result = new ArrayList<>();
        char start_char = content.charAt(0);
        if (start_char == ARRAY_START || start_char == PAYLOAD_START || start_char == STRING_START) {
            extractTillLastBracket(content, result, 0);
            return listToArray(result);
        }
        return content.split(COMMA_SEPRATOR);
    }

    public int extractTillClosingBracket(int start_idx, char close, char start_char, String content) {
        int end_idx = start_idx + 1;
        if (close != ' ') {
            Stack<Character> stack = new Stack<Character>();
            stack.add(start_char);
            while (!stack.isEmpty() && end_idx < content.length()) {
                if (content.charAt(end_idx) == '\\') {
                    end_idx++;
                } else if (content.charAt(end_idx) == close) {
                    stack.pop();
                } else if (content.charAt(end_idx) == start_char) {
                    stack.add(start_char);
                }
                end_idx++;
            }
            if (stack.isEmpty()) {
                return end_idx;
            }
        }
        return 0;
    }

    private void extractTillLastBracket(String content, List<String> result, int start_idx) {
        if (content.isEmpty())
            return;
        char start_char = content.charAt(0);
        char close = ' ';
        if (start_char == ARRAY_START) {
            close = ARRAY_END;
        } else if (start_char == PAYLOAD_START) {
            close = PAYLOAD_END;
        } else if (start_char == '\"') {
            close = '\"';
        }
        if (start_char == ' ' || start_char == ',') {
            extractTillLastBracket(content.substring(start_idx + 1), result, 0);
            return;
        }
        int closingBracketIdx = extractTillClosingBracket(start_idx, close, start_char, content);
        result.add(content.substring(start_idx, closingBracketIdx));
        extractTillLastBracket(content.substring(closingBracketIdx), result, 0);
        return;
    }

    private String[] listToArray(List<String> list) {
        String[] array = new String[list.size()];
        int i = 0;
        for (String str : list) {
            array[i] = str;
            i++;
        }
        return array;
    }

}
