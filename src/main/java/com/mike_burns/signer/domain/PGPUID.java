package com.mike_burns.signer.domain;

public class PGPUID
{
    private final String uidString;
    private final long creationDate;
    private final long expirationDate;
    private final PGPFlag flags;

    public PGPUID( String uidString, long creationDate, long expirationDate, PGPFlag flags )
    {
        this.uidString = uidString;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.flags = flags;
    }

    public String uidString()
    {
        return uidString;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        PGPUID pgpuid = (PGPUID) o;

        if ( creationDate != pgpuid.creationDate )
        {
            return false;
        }
        if ( expirationDate != pgpuid.expirationDate )
        {
            return false;
        }
        if ( flags != pgpuid.flags )
        {
            return false;
        }
        if ( uidString != null ? !uidString.equals( pgpuid.uidString ) : pgpuid.uidString != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = uidString != null ? uidString.hashCode() : 0;
        result = 31 * result + (int) (creationDate ^ (creationDate >>> 32));
        result = 31 * result + (int) (expirationDate ^ (expirationDate >>> 32));
        result = 31 * result + (flags != null ? flags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "PGPUID{" +
                "uidString='" + uidString + '\'' +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", flags=" + flags +
                '}';
    }
}
