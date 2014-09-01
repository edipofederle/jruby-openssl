/***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2006 Ola Bini <ola@ologix.com>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.ext.openssl.x509store;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.x500.X500Name;

import org.jruby.ext.openssl.SecurityHelper;

/**
 * c: X509_NAME
 *
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 */
public class Name {

    final X500Name name;

    public Name(final X500Principal principal) {
        this.name = X500Name.getInstance( principal.getEncoded() );
    }

    public Name(final X500Name name) {
        this.name = name;
    }

    private transient int hash = 0;

    public int hash() {
        if ( hash != 0 ) return hash;
        try {
            final byte[] bytes = name.getEncoded();
            MessageDigest md5 = SecurityHelper.getMessageDigest("MD5");
            final byte[] digest = md5.digest(bytes);
            int result = 0;
            result |= digest[3] & 0xff; result <<= 8;
            result |= digest[2] & 0xff; result <<= 8;
            result |= digest[1] & 0xff; result <<= 8;
            result |= digest[0] & 0xff;
            return hash = result & 0xffffffff;
        }
        catch (NoSuchAlgorithmException e) {
            return 0;
        }
        catch (IOException e) {
            return 0;
        }
        catch (RuntimeException e) {
            return 0;
        }
    }

    /**
     * c: X509_NAME_hash
     */
    @Override
    public int hashCode() { return hash(); }

    @Override
    public boolean equals(final Object that) {
        //if ( that instanceof X500Principal ) {
        //    return equals( (X500Principal) that );
        //}
        if ( that instanceof Name ) {
            return this.name.equals( ((Name) that).name );
        }
        return false;
    }

    public boolean equalTo(final X500Name name) {
        return this.name.equals(name);
    }

    public boolean equalTo(final X500Principal principal) {
        try {
            return new X500Principal(this.name.getEncoded(ASN1Encoding.DER)).equals(principal);
        }
        catch (IOException e) {
            return false;
        }
    }

    @Deprecated
    public boolean isEqual(final X500Principal principal) {
        return equalTo(principal);
    }

}// X509_NAME