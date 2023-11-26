import { createApp } from 'vue'
import { VueFire } from 'vuefire'
// @ts-ignore
import App from './App.vue'
import { firebaseApp } from './firebase'

const app = createApp(App)
app.use(VueFire, {
  firebaseApp
})

app.mount('#app')
