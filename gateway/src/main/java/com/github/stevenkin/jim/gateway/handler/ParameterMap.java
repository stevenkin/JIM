package com.github.stevenkin.jim.gateway.handler;

import java.util.*;

public class ParameterMap {
    private Map<String, List<String>> paramHashValues = new LinkedHashMap();

    public ParameterMap(String originalParam) {
        if (originalParam != null && !"".equals(originalParam.trim())) {
            String[] params = originalParam.split("&");
            String[] var3 = params;
            int var4 = params.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String param = var3[var5];
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    List<String> values = (List)this.paramHashValues.get(key);
                    if (values == null) {
                        values = new ArrayList();
                        this.paramHashValues.put(key, values);
                    }

                    ((List)values).add(value);
                }
            }

        }
    }

    public Map<String, List<String>> getParameterMap() {
        return this.paramHashValues;
    }

    public List<String> getParameterValues(String name) {
        return (List)this.paramHashValues.get(name);
    }

    public String getParameter(String name) {
        List<String> values = (List)this.paramHashValues.get(name);
        if (values != null) {
            return values.size() == 0 ? "" : (String)values.get(0);
        } else {
            return null;
        }
    }

    public Set<String> getParameterNames() {
        return this.paramHashValues.keySet();
    }
}

