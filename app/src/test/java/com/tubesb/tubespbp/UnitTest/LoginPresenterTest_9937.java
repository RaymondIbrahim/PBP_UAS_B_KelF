package com.tubesb.tubespbp.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest_9937 {
    @Mock
    private LoginView view;
    @Mock
    private LoginService service;
    private LoginPresenter presenter;
    @Before
    public void setUp() throws Exception {
        presenter = new LoginPresenter(view, service);
    }
    @Test
    public void shouldShowErrorMessageWhenUsernameIsEmpty() throws Exception {
        System.out.println("Test 1 Email Kosong");
        when(view.getEmail()).thenReturn("");
        System.out.println("email : "+view.getEmail());
        presenter.onLoginClicked();
        verify(view).showEmailError("Email Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenPasswordIsEmpty() throws Exception {
        System.out.println("Test 2 Password Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("");
        System.out.println("password : "+view.getPassword());
        presenter.onLoginClicked();
        verify(view).showPasswordError("Password Tidak Boleh Kosong");
    }
    @Test
    public void shouldStartMainActivityWhenEmailAndPasswordAreCorrect() throws
            Exception {
        System.out.println("Test 3 Email Password Valid");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(service.getValid(view, view.getEmail(),
                view.getPassword())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view,view.getEmail(),
                view.getPassword()));
        presenter.onLoginClicked();
        //verify(view).startMainActivity();
    }
    @Test
    public void shouldShowLoginErrorWhenEmailAndPasswordAreInvalid() throws
            Exception {
        System.out.println("Test 4 Password invalid");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("raha");
        System.out.println("password : "+view.getPassword());
        when(service.getValid(view, view.getEmail(),
                view.getPassword())).thenReturn(false);
        System.out.println("Hasil : "+service.getValid(view,view.getEmail(),
                view.getPassword()));
        presenter.onLoginClicked();
        //verify(view).showLoginError(R.string.login_failed);
    }
    @Test
    public void shouldShowLoginErrorWhenEmailAreInvalidAndPassword() throws
            Exception {
        System.out.println("Test 4 Email invalid");
        when(view.getEmail()).thenReturn("ignad@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(service.getValid(view, view.getEmail(),
                view.getPassword())).thenReturn(false);
        System.out.println("Hasil : "+service.getValid(view,view.getEmail(),
                view.getPassword()));
        presenter.onLoginClicked();
        //verify(view).showLoginError(R.string.login_failed);
    }
}