package ru.mail.kan;

import one.nio.http.HttpSession;
import one.nio.http.Request;
import one.nio.http.Response;
import org.jetbrains.annotations.NotNull;

public interface RequestProcessor {

    @NotNull
    Response process(Request request);

}
