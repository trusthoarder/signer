package com.trusthoarder.signer.domain;

public enum PGPFlag
{
    VALID( "" ),
    REVOKED( "r" ),
    DISABLED( "d" ),
    EXPIRED( "e" );

    private final String flagChar;

    PGPFlag( String flagChar )
    {
        this.flagChar = flagChar;
    }

    public static PGPFlag fromChar( char flag )
    {
        switch(flag)
        {
            case 'r': return REVOKED;
            case 'd': return DISABLED;
            case 'e': return EXPIRED;
            default: return VALID;
        }
    }
}
