/**
 * Copyright (c) 2005-2020, KoLmafia development team
 * http://kolmafia.sourceforge.net/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  [1] Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  [2] Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in
 *      the documentation and/or other materials provided with the
 *      distribution.
 *  [3] Neither the name "KoLmafia" nor the names of its contributors may
 *      be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION ) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE ) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sourceforge.kolmafia.textui.command;

import java.util.Set;

import net.sourceforge.kolmafia.KoLConstants.MafiaState;
import net.sourceforge.kolmafia.KoLmafia;

import net.sourceforge.kolmafia.objectpool.ItemPool;

import net.sourceforge.kolmafia.request.CargoCultistShortsRequest;

import net.sourceforge.kolmafia.session.InventoryManager;

import net.sourceforge.kolmafia.utilities.StringUtilities;

public class CargoCultCommand
	extends AbstractCommand
{
	public CargoCultCommand()
	{
		this.usage = " inspect | [pocket] - get status of Cargo Cult Shorts, or pick a pocket.";
	}

	@Override
	public void run( final String cmd, String parameters )
	{
		if ( InventoryManager.getAccessibleCount( ItemPool.CARGO_CULTIST_SHORTS ) == 0 )
		{
			KoLmafia.updateDisplay( MafiaState.ERROR, "You don't own a pair of Cargo Cultist Shorts" );
			return;
		}

		if ( parameters.equals( "" ) )
		{
			this.printPockets();
			return;
		}

		if ( parameters.equals( "inspect" ) )
		{
			CargoCultistShortsRequest visit = new CargoCultistShortsRequest();
			visit.run();

			if ( !KoLmafia.permitsContinue() )
			{
				return;
			}

			this.printPockets();
			return;
		}

		if ( StringUtilities.isNumeric( parameters ) )
		{
			int pocket = StringUtilities.parseInt( parameters );

			if ( pocket < 1 || pocket > 666 )
			{
				KoLmafia.updateDisplay( MafiaState.ERROR, "Pocket must be from 1-666" );
				return;
			}

			CargoCultistShortsRequest pick = new CargoCultistShortsRequest( pocket );
			pick.run();
			return;
		}

		KoLmafia.updateDisplay( MafiaState.ERROR, "Specify a pocket # from 1-666" );
	}

	private void printPockets()
	{
		Set<Integer> pockets = CargoCultistShortsRequest.pickedPockets;
		if ( pockets.size() == 0 )
		{
			KoLmafia.updateDisplay( "You have not picked any pockets yet during this ascension." );
			return;
		}

		KoLmafia.updateDisplay( "You have picked the following pockets during this ascension:" );
		for ( Integer pocket : pockets )
		{
			KoLmafia.updateDisplay( String.valueOf( pocket ) );
		}
	}

}