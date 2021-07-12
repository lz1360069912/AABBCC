<template>
  <big-file v-bind:input-id="inputId"
            v-bind:text="text"
            v-bind:suffixs="suffixs"
            v-bind:use="use"
            v-bind:after-upload="afterUpload"
            v-bind:shard-size="1000 * 1024 * 1024"
            v-bind:url="'oss-append'"></big-file>
</template>

<script>
import BigFile from "./big-file";

export default {
  components: {BigFile},
  name: 'vod',
  // props，用于父子组件传递数据，可以理解成组件可配置的属性
  props: {
    text: {
      default: "上传VOD"
    },
    inputId: {
      default: "vod-upload"
    },
    use: {
      default: ""
    },
    suffixs: {
      default: []
    },
    shardSize: {
      default: 50 * 1024
    },
    url: {
      default: "oss-append"
    },
    afterUpload: {
      type: Function,
      default: null
    }
  }
}
</script>
