package com.stasroshchenko.clinic.util;

import com.stasroshchenko.clinic.entity.person.DoctorData;

import java.time.LocalDate;

/**
 * Util final class for generating doctor's biography.
 * @author staffsterr2000
 * @version 1.0
 */
public final class DoctorDescriptionHelper {

    /**
     * Generates text-biography based on doctor's data.
     * @param doctorData doctor's data
     * @return doctor's biography
     * @since 1.0
     */
    public static String getDescriptionForDoctor(DoctorData doctorData) {
        String lastName = doctorData.getLastName();
        Integer experience = doctorData.getExperienceAge();

        return String.format("Dr. %1$s joined the DeDentist? Clinic staff in %2$d. He received his medical degree from" +
                "the University of Oklahoma College of Medicine.%nHe then completed his Internal Medicine training" +
                "at the University of Oklahoma in Oklahoma City.%nDr. %1$s went on to complete his fellowship training" +
                "in Allergy and Immunology at University of Texas Medical Branch in Galveston.%n" +
                "Dr. %1$s enjoys spending time with his family and resides in Edmond, Oklahoma.%nHis hobbies include exercise, " +
                "golfing and snow skiing.", lastName, LocalDate.now().getYear() - experience);
    }

}
