package com.stasroshchenko.clinic.model.service.user;

import com.stasroshchenko.clinic.entity.person.ClientData;
import com.stasroshchenko.clinic.entity.user.ApplicationUserClient;
import com.stasroshchenko.clinic.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.clinic.entity.person.DoctorData;
import com.stasroshchenko.clinic.entity.user.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Delegates all the processing to {@link ApplicationUserServiceReal}.
 * Used for renaming some overridden methods of real service.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ApplicationUserService {

    /**
     * Real service
     */
    private final ApplicationUserServiceReal applicationUserServiceReal;



    /**
     * Gets user by its username.
     * @param username username of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUser getByUsername(String username) {
        return applicationUserServiceReal.loadUserByOnlyUsername(username);
    }



    /**
     * Gets user by its email.
     * @param email email of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUser getByEmail(String email) {
        return applicationUserServiceReal.loadUserByOnlyEmail(email);
    }



    /**
     * Gets user by its client data.
     * @param clientData client data of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUserClient getByClientData(ClientData clientData) {
        return applicationUserServiceReal.loadUserByClientData(clientData);
    }



    /**
     * Gets user by its doctor data.
     * @param doctorData doctor data of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUserDoctor getByDoctorData(DoctorData doctorData) {
        return applicationUserServiceReal.loadUserByDoctorData(doctorData);
    }



    /**
     * Gets list of all client users.
     * @return list of all client users.
     * @since 1.0
     */
    public List<ApplicationUserClient> getAllUserClients() {
        return applicationUserServiceReal.loadAllUserClients();
    }



    /**
     * Gets list of all doctor users.
     * @return list of all doctor users.
     * @since 1.0
     */
    public List<ApplicationUserDoctor> getAllUserDoctors() {
        return applicationUserServiceReal.loadAllUserDoctors();
    }



    /**
     * Checks for username and email absence in DB.
     * @param rawUser raw user without password encoding.
     * @return bool of the absence (true - absent).
     * @since 1.0
     */
    public boolean isUsernameAndEmailAbsent(ApplicationUser rawUser) {
        return applicationUserServiceReal.isUsernameAndEmailAbsent(rawUser);
    }



    /**
     * Checks for username and email absence in DB. Causes
     * exception if user with such data is present.
     * @param rawUser applicationUser to check is this rawUser is in the base
     * @return rawUser if this user is not in the base
     * @throws IllegalStateException if this username/email has already been taken
     * @since 1.0
     */
    public ApplicationUser checkUsernameAndEmailPresence(ApplicationUser rawUser) {
        return applicationUserServiceReal.checkUsernameAndEmailPresence(rawUser);
    }



    /**
     * Seeks the user by its email and enables the user.
     * @param email user's email by which user will be found
     *              and then the user will be enabled.
     * @since 1.0
     */
    public void enableUserByEmail(String email) {
        applicationUserServiceReal.enableUserByEmail(email);
    }



    /**
     * Encodes the user instance and signs it up.
     * @param rawUser raw user without password encoding.
     * @return confirmation token UUID.
     * @since 1.0
     */
    public String signUpUser(ApplicationUser rawUser) {
        return applicationUserServiceReal.signUpUser(rawUser);
    }



    /**
     * Deletes old one token, creates new one and returns it.
     * @param user user who needs new token.
     * @return new confirmation token UUID.
     * @since 1.0
     */
    public String resendConfirmationToken(ApplicationUser user) {
        return applicationUserServiceReal.resendConfirmationToken(user);
    }

}
