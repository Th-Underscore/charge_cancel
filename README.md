# Cancel Charge

Cancels any item charge when the Attack key is pressed.

## Motivation

I wanted to cancel bow charges in the offhand without having to switch back to the mainhand or wait for the draw to complete.

## Usage

While holding use (right-click) on any chargeable item like a bow, press Attack (left-click) to instantly cancel the charge.

## Compatibility

- Mainhand or offhand
- Any chargeable item (bows, crossbows, tridents, etc.)
- Rebound controls
- Modded items
- Server-side compatible
- Multiplayer safe
- Forge 1.20.1

## Technical Details

The mod uses Forge Mixin to intercept the attack input client-side and sends a packet to the server to call `player.stopUsingItem()`. Reflection is used to clear the use item field on the client immediately for responsive feedback.
