package com.mike_burns.signer.domain;

import java.util.ArrayList;
import java.util.List;

public class PGPKey
{

    private final String keyId;
    private final int algorithm;
    private final int keyLength;
    private final long creationDate;
    private final long expirationDate;
    private final PGPFlag state;

    private final List<PGPUID> uids;

    public PGPKey( PGPKey baseKey, PGPUID additionalUid )
    {
        this.keyId = baseKey.keyId;
        this.algorithm = baseKey.algorithm;
        this.keyLength = baseKey.keyLength;
        this.creationDate = baseKey.creationDate;
        this.expirationDate = baseKey.expirationDate;
        this.state = baseKey.state;
        this.uids = new ArrayList<>( baseKey.uids );
        this.uids.add( additionalUid );
    }

    public PGPKey( String keyId, PGPFlag state, int algorithm, int keyLength, long creationDate, long expirationDate, List<PGPUID> uids )
    {
        this.keyId = keyId;
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.state = state;
        this.uids = uids;
    }

    public String keyId()
    {
        return keyId;
    }

    public List<PGPUID> uids()
    {
        return uids;
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

        PGPKey pgpKey = (PGPKey) o;

        if ( algorithm != pgpKey.algorithm )
        {
            return false;
        }
        if ( creationDate != pgpKey.creationDate )
        {
            return false;
        }
        if ( expirationDate != pgpKey.expirationDate )
        {
            return false;
        }
        if ( keyLength != pgpKey.keyLength )
        {
            return false;
        }
        if ( keyId != null ? !keyId.equals( pgpKey.keyId ) : pgpKey.keyId != null )
        {
            return false;
        }
        if ( state != pgpKey.state )
        {
            return false;
        }
        if ( uids != null ? !uids.equals( pgpKey.uids ) : pgpKey.uids != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = keyId != null ? keyId.hashCode() : 0;
        result = 31 * result + algorithm;
        result = 31 * result + keyLength;
        result = 31 * result + (int) (creationDate ^ (creationDate >>> 32));
        result = 31 * result + (int) (expirationDate ^ (expirationDate >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (uids != null ? uids.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "PGPKey{" +
                "keyId='" + keyId + '\'' +
                ", algorithm=" + algorithm +
                ", keyLength=" + keyLength +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", state=" + state +
                ", uids=" + uids +
                '}';
    }
}
