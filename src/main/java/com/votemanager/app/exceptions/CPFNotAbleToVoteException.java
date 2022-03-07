package com.votemanager.app.exceptions;

public class CPFNotAbleToVoteException extends RuntimeException {

    public CPFNotAbleToVoteException(String message){
        super(message);
    }
}
