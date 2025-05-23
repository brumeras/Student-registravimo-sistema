/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.poifs.storage;

import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.util.LittleEndianConsts;

/**
 * Constants used in reading/writing the Header block
 */
public interface HeaderBlockConstants
{
    long _signature               = 0xE11AB1A1E011CFD0L;
    int  _bat_array_offset        = 0x4c;
    int  _max_bats_in_header      =
        (POIFSConstants.SMALLER_BIG_BLOCK_SIZE - _bat_array_offset)
        / LittleEndianConsts.INT_SIZE; // If 4k blocks, rest is blank

    // Note - in Microsoft terms:
    //  BAT ~= FAT
    //  SBAT ~= MiniFAT
    //  XBAT ~= DIFat

    // useful offsets
    int  _signature_offset        = 0;
    int  _bat_count_offset        = 0x2C;
    int  _property_count_offset   = 0x28;
    int  _property_start_offset   = 0x30;
    int  _sbat_start_offset       = 0x3C;
    int  _sbat_block_count_offset = 0x40;
    int  _xbat_start_offset       = 0x44;
    int  _xbat_count_offset       = 0x48;
}

