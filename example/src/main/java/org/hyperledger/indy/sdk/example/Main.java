/*
 * Copyright (c) 2017 Evernym Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.hyperledger.indy.sdk.example;

import java.io.File;

import org.hyperledger.indy.sdk.jwrapper.IndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyResult;
import org.hyperledger.indy.sdk.jwrapper.pool.PoolApi;

public class Main {
  
  public static void main(String[] args) {
    IndyApi api = new IndyApi(new File("./libindy.dylib"));

    PoolApi poolApi = new PoolApi(api);
    
    String configName = "vagrant_pool_sandbox";
    
    IndyResult result = poolApi.createPoolLedgerConfig(configName, null);
    System.out.println("Returnvalue: " + result.getReturnValue() + " ErrorCode: " + result.getErrorCode());
  }
}
