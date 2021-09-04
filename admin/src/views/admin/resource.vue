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
    <br>
    <pagination ref="pagination" v-bind:list="list"></pagination>

    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>id</th>
        <th>名称</th>
        <th>页面</th>
        <th>请求</th>
        <th>父id</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="resource in resources">
        <td>{{ resource.id }}</td>
        <td>{{ resource.name }}</td>
        <td>{{ resource.page }}</td>
        <td>{{ resource.request }}</td>
        <td>{{ resource.parent }}</td>
        <td>
          <div class="btn-group">
            <button v-on:click="edit(resource)" class="btn btn-white btn-xs btn-info btn-round">
              编辑
            </button>
            &nbsp;
            <button v-on:click="del(resource.id)" class="btn btn-white btn-xs btn-warning btn-round">
              删除
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import Pagination from "../../components/pagination";

export default {
  components: {Pagination},
  name: "system-resource",
  data: function () {
    return {
      resource: {}, // 用于绑定form表单的数据
      resources: [],
      resourceStr: ""
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("system-resource-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    _this.list(1);
  },
  methods: {
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/system/admin/resource/list", {
        page: page,
        size: _this.$refs.pagination.size
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.resources = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
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
    }
  }
}
</script>