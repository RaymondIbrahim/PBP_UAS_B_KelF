package com.tubesb.tubespbp.UnitTestingRegister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegisterPresenterTest {
    @Mock
    private RegisterView view;
    @Mock
    private RegisterService service;
    private RegisterPresenter presenter;
    @Before
    public void setUp() throws Exception {
        presenter = new RegisterPresenter(view, service);
    }
    @Test
    public void shouldShowErrorMessageWhenEmailIsEmpty() throws Exception {
        System.out.println("Test 1 Email Kosong");
        when(view.getEmail()).thenReturn("");
        System.out.println("email : "+view.getEmail());
        presenter.onRegisterClicked();
        verify(view).showEmailError("Email tidak boleh kosong");
    }
    @Test

    public void shouldShowErrorMessageWhenEmailAreinvalid() throws Exception {
        System.out.println("Test 2 Email Invalid");
        when(view.getEmail()).thenReturn("igna");
        System.out.println("email : "+view.getEmail());
        presenter.onRegisterClicked();
        verify(view).showEmailError("Email harus sesuai struktur");
    }

    @Test
    public void shouldShowErrorMessageWhenPasswordIsEmpty() throws Exception {
        System.out.println("Test 3 Password Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("");
        System.out.println("password : "+view.getPassword());
        presenter.onRegisterClicked();
        verify(view).showPasswordError("Password tidak boleh kosong");
    }

    @Test
    public void shouldShowLoginErrorWhenPasswordAreInvalid() throws
            Exception {
        System.out.println("Test 4 Password invalid");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("raha");
        System.out.println("password : "+view.getPassword());
        presenter.onRegisterClicked();
        verify(view).showPasswordError("Password tidak boleh kurang dari 6 karakter");
    }

    @Test
    public void shouldShowErrorMessageWhenNamaIsEmpty() throws Exception {
        System.out.println("Test 5 Nama Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(view.getNama()).thenReturn("");
        System.out.println("nama : "+view.getNama());
        presenter.onRegisterClicked();
        verify(view).showNamaError("Nama tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorMessageWhenAlamatIsEmpty() throws Exception {
        System.out.println("Test 6 Alamat Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(view.getNama()).thenReturn("Ignatius Dimas");
        System.out.println("nama : "+view.getNama());
        when(view.getAlamat()).thenReturn("");
        System.out.println("alamat : "+view.getAlamat());
        presenter.onRegisterClicked();
        verify(view).showAlamatError("Alamat tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorMessageWhenTelpIsEmpty() throws Exception {
        System.out.println("Test 7 No Telepon Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(view.getNama()).thenReturn("Ignatius Dimas");
        System.out.println("nama : "+view.getNama());
        when(view.getAlamat()).thenReturn("Magelang Jawa Tengah");
        System.out.println("alamat : "+view.getAlamat());
        when(view.getnoTelp()).thenReturn("");
        System.out.println("no Telepon : "+view.getnoTelp());
        presenter.onRegisterClicked();
        verify(view).shownoTelpError("No Telepon tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorMessageWhenTelpInvalid() throws Exception {
        System.out.println("Test 8 No Telepon Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(view.getNama()).thenReturn("Ignatius Dimas");
        System.out.println("nama : "+view.getNama());
        when(view.getAlamat()).thenReturn("Magelang Jawa Tengah");
        System.out.println("alamat : "+view.getAlamat());
        when(view.getnoTelp()).thenReturn("081231");
        System.out.println("no Telepon : "+view.getnoTelp());
        presenter.onRegisterClicked();
        verify(view).shownoTelpError("No Telepon tidak boleh kurang dari 12 digit");
    }

    @Test
    public void shouldShowErrorMessageWhenAllInputRegisterCorrect() throws Exception {
        System.out.println("Test 9 No Telepon Kosong");
        when(view.getEmail()).thenReturn("ignadimas@gmail.com");
        System.out.println("email : "+ view.getEmail());
        when(view.getPassword()).thenReturn("rahasia");
        System.out.println("password : "+view.getPassword());
        when(view.getNama()).thenReturn("Ignatius Dimas");
        System.out.println("nama : "+view.getNama());
        when(view.getAlamat()).thenReturn("Magelang Jawa Tengah");
        System.out.println("alamat : "+view.getAlamat());
        when(view.getnoTelp()).thenReturn("081321827564");
        System.out.println("no Telepon : "+view.getnoTelp());
        when(service.getValid(view, view.getEmail(), view.getPassword(), view.getNama(),
                view.getAlamat(),view.getnoTelp())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view,view.getEmail(),
                view.getPassword(), view.getNama(), view.getAlamat(),view.getnoTelp()));
        presenter.onRegisterClicked();
    }
}