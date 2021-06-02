<template>
  <div>
    <h1>
      欢迎
    </h1>
  </div>
</template>

<script>
export default {
  name: "welcome",
  mounted: function () {
    this.$parent.activeSidebar("welcome-sidebar");
  },
  methods: {
  }
}
</script>