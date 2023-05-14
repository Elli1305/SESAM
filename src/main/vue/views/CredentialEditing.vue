<template>
  {{typeof props.id === 'undefined' ? 'Create a new Credential' : `Edit Credential ${props.id}`}}
</template>

<script lang="ts" setup>
const props = defineProps<{ id?: number }>();
</script>

<style scoped></style>