if (cm.getBossLog('PANDORA') < 1) {
  cm.gainItem(cm.getPandoraItem(Math.floor(Math.random() * cm.getMaxItems()) + 1)); // Gains a random item.
cm.setPandoraLog('PANDORA');
cm.dispose();
} else {
  cm.sendNext("Has utilizado todos tus usuarios. Por favor vuelve manana!");
cm.dispose();