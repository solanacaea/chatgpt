import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import Antd from 'ant-design-vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import router from './components/core/router';

const app = createApp(App);
app.use(VueAxios, axios);
app.use(router);
app.use(Antd);
app.mount('#app')
