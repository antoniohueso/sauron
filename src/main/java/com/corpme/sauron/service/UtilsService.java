package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by ahg on 15/12/14.
 */
@Service
public class UtilsService {

    void validateRequired(Object value,String message) {
        if (value == null) throw new ApplicationException(message);

        if (value instanceof String) {
            String str = (String) value;
            if (str == null || str.trim().length() == 0) throw new ApplicationException(message);
        }

        if (value instanceof Set) {
            Set set = (Set) value;
            if (set.size() == 0) throw new ApplicationException(message);
        }
    }
}
