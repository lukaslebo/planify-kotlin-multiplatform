import {instantiate} from './planify.uninstantiated.mjs';

await wasmSetup;
instantiate({skia: Module['asm']});
