package com.java.fiap.ordermanager.domain.service.usecases;

public interface UseCase<I, O> {

  O execute(I input);
}
