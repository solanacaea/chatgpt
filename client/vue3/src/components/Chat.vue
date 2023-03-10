<script setup lang="ts">
import { useRoute } from 'vue-router';
import {
  SendOutlined,
  DislikeFilled,
  LikeFilled,
  LikeOutlined,
  DislikeOutlined
} from '@ant-design/icons-vue';
import axios from 'axios';
import { ref } from 'vue';
import { LayoutSider } from 'ant-design-vue';

const question = ref('');
const route = useRoute();
const conversation = route.params.conversation;
const questions = ref([]);

const sendQuestion = () => {
  questions.value.push({
    question: question.value,
    reply: ''
  } as never);

  axios.request({
    url: '/api/chat',
    method: 'POST',
    data: { "question": question.value },
  }).then((res) => {
    // questions.value.push({
    //   question: question.value,
    //   reply: res.data
    // } as never);
    const last = questions.value.at(questions.value.length - 1)
    last.reply = res.data
    question.value = '';
  });
}
</script>

<template>
  <div class="h-100 w-100 p-3">
    <div class="d-flex flex-column h-100">
      <div class="flex-fill">
        <div v-for="(question) in questions">
          <!-- Question -->
          <a-card class="shadow">
            <a-comment>
              <template #author><a>Me</a></template>
              <template #avatar>
                <a-avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" alt="Han Solo" />
              </template>
              <template #content>
                <p>
                  {{ question.question }}
                </p>
              </template>
            </a-comment>
          </a-card>

          <!-- Reply -->
          <a-card class="my-3 shadow" theme="dark">
            <a-comment>
              <template #author><a>AI</a></template>
              <template #avatar>
                <a-avatar src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png" alt="Han Solo" />
              </template>
              <template #content>
                <p>
                  {{ question.reply }}
                </p>
              </template>
            </a-comment>
          </a-card>

        </div>

      </div>
      <div class="d-flex">
        <a-textarea placeholder="Questions" :rows="1" v-model:value="question"></a-textarea>
        <a-button type="dashed" @click="sendQuestion">
          <template #icon>
            <SendOutlined></SendOutlined>
          </template>
        </a-button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
