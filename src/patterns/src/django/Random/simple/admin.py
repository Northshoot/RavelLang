from django.contrib import admin

from .models import Originator, SimpleModelMeta, SimpleModel

admin.site.register(Originator)
admin.site.register(SimpleModelMeta)
admin.site.register(SimpleModel)
