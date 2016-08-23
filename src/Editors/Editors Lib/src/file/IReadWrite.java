/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Gil
 */
public interface IReadWrite {
    public void writeData(DataOutputStream dos) throws IOException;
    public void readData(DataInputStream dis) throws IOException;
}
