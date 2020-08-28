package com.example.template.pojo.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * The response for UI to show info when we parse the uploaded cvs files containing
 * member ids as "targets" of a promotion(dedicated to this group of users)
 *
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TargetsValidationResponse {
    /**
     * Error message returned by validator when cvs file is malformatted
     * For UI
     */
    private String error;

    /**
     * Valid(found) member ids. Subsequent promotion creation request needs this info as "targets".
     */
    private Set<String> validTargets;

    /**
     * Number of valid member ids(Informative only, for UI)
     */
    private Integer processed;

    /**
     * Invalid(missing) member ids. For UI
     * Empty = no error
     */
    private Set<String> invalidTargets;
}
