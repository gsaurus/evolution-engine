/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

import file.*;
import game.GameInvariants;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Gil
 */
public class LangEncoder extends FileEncoder<LanguagePack>{
    
    public LangEncoder(){
        super(GameInvariants.LANGUAGE_EXTENSION, GameInvariants.LANGUAGE_VERSION);
        this.setFileData(new LanguagePack(versionID));
    }
    

    
     @Override
    public void readData(DataInputStream dis) throws IOException {
        this.setFileData(new LanguagePack(versionID));
        super.readData(dis);
    }
    

}
