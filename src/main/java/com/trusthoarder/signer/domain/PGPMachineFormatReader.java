package com.trusthoarder.signer.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PGPMachineFormatReader
{
    public List<PGPKey> read(BufferedReader content) throws IOException
    {
        List<PGPKey> keys = new ArrayList<>();
        PGPKey currentKey = null;

        String line;
        while( (line = content.readLine()) != null )
        {
            String[] parts = line.split( ":" , -1);
            if(parts.length < 1)
            {
                continue;
            }
            String type = parts[0];
            if(type.equals( "pub" ))
            {
                if(currentKey != null)
                {
                    keys.add( currentKey );
                }
                currentKey = new PGPKey( parts[1],
                        PGPFlag.fromChar( parts[6].length() == 1 ? parts[6].charAt( 0 ) : 'v'),
                        parseInt( parts[2] ),
                        parseInt( parts[3] ),
                        parseLong( parts[4] ),
                        parseLong( parts[5] ),
                        new ArrayList<PGPUID>() );
            }
            else if(type.equals( "uid" ) && currentKey != null)
            {
                currentKey = new PGPKey( currentKey,
                    new PGPUID( parts[1],
                        parseLong( parts[2] ),
                        parseLong( parts[3] ),
                        PGPFlag.fromChar( parts[4].length() == 1 ? parts[4].charAt( 0 ) : 'v')));
            }
            else
            {
                // Ignore
            }
        }
        if(currentKey != null)
        {
            keys.add( currentKey );
        }
        return keys;
    }

    private long parseLong( String part )
    {
        return part == null || part.length() == 0 ? -1 : Long.parseLong( part );
    }

    private int parseInt( String part )
    {
        return part == null || part.length() == 0 ? -1 : Integer.parseInt( part );
    }


}
