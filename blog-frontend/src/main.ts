import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { vMotion } from './directives/vMotion'
import './style.css'

const app = createApp(App)
app.directive('motion', vMotion)
app.use(createPinia())
app.use(router)
app.mount('#app')
