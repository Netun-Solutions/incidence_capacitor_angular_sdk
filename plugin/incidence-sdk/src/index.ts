import { registerPlugin } from '@capacitor/core';

import type { IncidencePlugin } from './definitions';

const Incidence = registerPlugin<IncidencePlugin>('Incidence', {
  web: () => import('./web').then(m => new m.IncidenceWeb()),
});

export * from './definitions';
export { Incidence };


