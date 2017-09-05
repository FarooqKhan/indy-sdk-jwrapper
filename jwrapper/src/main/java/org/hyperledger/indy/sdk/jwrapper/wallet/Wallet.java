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
package org.hyperledger.indy.sdk.jwrapper.wallet;

/**
 * @version 1.0 04-Sep-2017
 */
public class Wallet {
  private int walletHandle;
  private String walletName;
  private WalletStatus status = WalletStatus.UNUSED;
  
  private String walletType;
  private String walletConfigJson;

  public Wallet(String walletName) {
    super();
    this.walletName = walletName;
  }

  public int getWalletHandle() {
    return walletHandle;
  }
  public void setWalletHandle(int walletHandle) {
    this.walletHandle = walletHandle;
  }

  public String getWalletName() {
    return walletName;
  }
  public void setWalletName(String walletName) {
    this.walletName = walletName;
  }

  public WalletStatus getStatus() {
    return status;
  }
  public void setStatus(WalletStatus status) {
    this.status = status;
  }

  public String getWalletType() {
    return walletType;
  }
  public void setWalletType(String walletType) {
    this.walletType = walletType;
  }

  public String getWalletConfigJson() {
    return walletConfigJson;
  }
  public void setWalletConfigJson(String walletConfigJson) {
    this.walletConfigJson = walletConfigJson;
  }
}
