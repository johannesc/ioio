/*
 * Copyright 2011 Ytai Ben-Tsvi. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ARSHAN POURSOHI OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 */

#ifndef __FEATURES_H__
#define __FEATURES_H__

#include "GenericTypeDefs.h"

#define IND_DBG_PIN 20
#define IND_DBG2_PIN 21
#define IND_BUTTON_PIN 19

#define IND_LEFT_Q0_PIN 13 //RD4
#define IND_LEFT_Q1_PIN 12 //RD3
#define IND_LEFT_Q2_PIN 11 //RD2

#define IND_LEFT_STCP_PIN 18 //RF1

void SetPinDigitalOut(int pin, int value, int open_drain);
void SetPinDigitalIn(int pin, int pull);
void SetPinAnalogIn(int pin);
void SetPinCapSense(int pin);
void SetPinPwm(int pin, int pwm_num, int enable);
void SetPinUart(int pin, int uart_num, int dir, int enable);
void SetPinSpi(int pin, int spi_num, int mode, int enable);
void SetPinInCap(int pin, int incap_num, int enable);
void HardReset();
void SoftReset();
void CheckInterface(BYTE interface_id[8]);
void IndSetButtonMask(BYTE left_button_mask, BYTE right_button_mask);
void IndHandlePinChange(BYTE pin, BYTE value);

#endif  // __FEATURES_H__
