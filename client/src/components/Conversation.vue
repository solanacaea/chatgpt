<script setup lang="ts">
import {
  MessageOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';

const conversations = ['Title: Clearify User Request', 'No appropriate title' , 'Helper Offered']

</script>

<template>
  <div class="h-100 w-100 d-flex">
    <a-menu style="width: 256px" theme="dark" mode="inline" class="h-100">
      <a-menu-item key="newChat">
        <template #icon>
          <PlusOutlined />
        </template>
        <span>New Chat</span>
      </a-menu-item>
      <a-menu-item :key="index" v-for="(conversation, index) in conversations">
        <template #icon>
          <MessageOutlined />
        </template>
        <router-link :to="conversation"> {{ conversation }} </router-link>
      </a-menu-item>
    </a-menu>
    <div class="flex-fill overflow-auto">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
</style>
