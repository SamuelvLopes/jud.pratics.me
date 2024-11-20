import { createApp } from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import { loadFonts } from './plugins/webfontloader';
import router from './router'; 
import store from './store';  // Agora essa linha é válida

loadFonts();

createApp(App)
  .use(vuetify)
  .use(router)    
  .use(store)
  .mount('#app');

