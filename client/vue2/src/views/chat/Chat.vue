<script>
const conversations = ['Title: Clearify User Request', 'No appropriate title', 'Helper Offered']
export default {
    data () {
        return {
            conversations: conversations
        }
    }
}

</script>

<template>
  <div style="width: 100%; height: 100%; display: flex">
    <a-menu style="width: 256px; height: 100%;" theme="dark" mode="inline">
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
    <div style="flex-grow: 1">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
</style>
