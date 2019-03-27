/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

import { DidKey } from '@decentralized-identity/did-common-typescript';

/**
 * Interface defining methods and properties to
 * be implemented by specific key stores.
 */
export default interface IKeyStore {
  /**
   * Returns the key associated with the specified
   * key identifier.
   * @param keyIdentifier for which to return the key.
   */
  get (keyIdentifier: string): Promise<Buffer | DidKey>;

  /**
   * Saves the specified key to the key store using
   * the key identifier.
   * @param keyIdentifier for the key being saved.
   * @param key being saved to the key store.
   */
  save (keyIdentifier: string, key: Buffer | DidKey): Promise<void>;
}
