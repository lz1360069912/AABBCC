<template>
  <div>
    <p>
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <div class="row">
      <div class="col-md-6">
        <textarea id="resource-textarea" class="form-control" v-model="resourceStr" name="resource"
                  rows="10"></textarea>
        <br>
        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="save()">
          <i class="ace-icon fa fa-plus blue"></i>
          保存
        </button>

      </div>
      <div class="col-md-6">
        <ul id="tree" class="ztree"></ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "system-resource",
  data: function () {
    return {
      resource: {}, // 用于绑定form表单的数据
      resources: [],
      resourceStr: "",
      tree: {}
    }
  },
  mounted: function () {
    let _this = this;
    _this.list();
  },
  methods: {
    /**
     * 列表查询
     * @param page
     */
    list() {
      let _this = this;
      Loading.show();
      _this.$ajax.get(process.env.VUE_APP_SERVER + "/system/admin/resource/load-tree").then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.resources = resp.content;
        // 初始化树
        _this.initTree();
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (Tool.isEmpty(_this.resourceStr)) {
        Toast.warning("资源不能为空！");
        return;
      }
      let json = JSON.parse(_this.resourceStr);
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/system/admin/resource/save", json).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/system/admin/resource/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    },

    /**
     * 初始资源树
     */
    initTree() {
      let _this = this;
      let setting = {
        data: {
          simpleData: {
            idKey: "id",
            pIdKey: "parent",
            rootPId: "",
            // enable: true
          }
        }
      };

      _this.zTree = $.fn.zTree.init($("#tree"), setting, _this.resources);
      _this.zTree.expandAll(true);
    }
  }
}
</script>