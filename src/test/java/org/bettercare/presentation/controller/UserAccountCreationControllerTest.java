package org.bettercare.presentation.controller;

// These tests check the account creation page logic

import jakarta.servlet.http.HttpSession;
import org.bettercare.domain.model.UserAccount;
import org.bettercare.business.service.UserAccountService;
import org.bettercare.business.service.NotificationService;
import org.bettercare.presentation.viewmodel.CreationViewModel;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAccountCreationControllerTest {

    @Test
    void redirectsToTheHomeRouteAfterCreatingAnAccount() {
        UserAccountService service = mock(UserAccountService.class);
        NotificationService notificationService = mock(NotificationService.class);
        UserAccountCreationController controller = new UserAccountCreationController(service, notificationService);
        CreationViewModel viewModel = validViewModel();
        UserAccount createdAccount = new UserAccount();
        HttpSession session = mock(HttpSession.class);

        when(service.findByName("new-user")).thenReturn(null, createdAccount);

        String view = controller.handleUserAccountCreation(
                new ExtendedModelMap(), session, viewModel,
                new BeanPropertyBindingResult(viewModel, "creationVM")
        );

        assertEquals("redirect:/", view);
        verify(service).insertUserAccount(org.mockito.ArgumentMatchers.any(UserAccount.class));
        verify(session).setAttribute("user", createdAccount);
        verify(notificationService).createNotification(
                org.mockito.ArgumentMatchers.eq(createdAccount),
                org.mockito.ArgumentMatchers.contains("Welcome to BetterCare"),
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void showsAFieldErrorInsteadOfAWhitelabelPageForADuplicateUsername() {
        UserAccountService service = mock(UserAccountService.class);
        UserAccountCreationController controller = new UserAccountCreationController(service, mock(NotificationService.class));
        CreationViewModel viewModel = validViewModel();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(viewModel, "creationVM");

        when(service.findByName("new-user")).thenReturn(new UserAccount());

        String view = controller.handleUserAccountCreation(
                new ExtendedModelMap(), mock(HttpSession.class), viewModel, bindingResult
        );

        assertEquals("userAccountCreation", view);
        assertTrue(bindingResult.hasFieldErrors("name"));
        verify(service, never()).insertUserAccount(org.mockito.ArgumentMatchers.any(UserAccount.class));
    }

    @Test
    void handlesARacingDuplicateInsertAsAFieldError() {
        UserAccountService service = mock(UserAccountService.class);
        UserAccountCreationController controller = new UserAccountCreationController(service, mock(NotificationService.class));
        CreationViewModel viewModel = validViewModel();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(viewModel, "creationVM");

        when(service.findByName("new-user")).thenReturn(null);
        org.mockito.Mockito.doThrow(new DuplicateKeyException("duplicate"))
                .when(service).insertUserAccount(org.mockito.ArgumentMatchers.any(UserAccount.class));

        String view = controller.handleUserAccountCreation(
                new ExtendedModelMap(), mock(HttpSession.class), viewModel, bindingResult
        );

        assertEquals("userAccountCreation", view);
        assertTrue(bindingResult.hasFieldErrors("name"));
    }

    private CreationViewModel validViewModel() {
        CreationViewModel viewModel = new CreationViewModel();
        viewModel.setName("new-user");
        viewModel.setEmail("new-user@example.com");
        viewModel.setPassword("password123");
        return viewModel;
    }
}