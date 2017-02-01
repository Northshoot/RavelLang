from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

from .models import Originator, SimpleModelMeta, SimpleModel
# Create your views here.

def index(request):
    return HttpResponse("Hello, world.")

#TODO: fix the csrf cookie mess
@csrf_exempt
def pushModel(request):
    #unmangle the data
    if request.method == 'GET':
        print("GET not supported")
    elif request.method == 'POST':
        print("POSTING")
        data =  bytearray(request.body)
        src =int.from_bytes(data[:4], byteorder='big')
        dst =int.from_bytes(data[4:8], byteorder='big')
        reserved = int.from_bytes(data[8:12], byteorder='big')
        model_id = int.from_bytes(data[12:16], byteorder='big')
        indx = int.from_bytes(data[16:20], byteorder='big')
        field1 = int.from_bytes(data[20:24], byteorder='big')
        field2 = int.from_bytes(data[24:28], byteorder='big')
        field3 = int.from_bytes(data[28:32], byteorder='big')
        field4 = int.from_bytes(data[32:36], byteorder='big')
        orig, created = Originator.objects.get_or_create(
            device_id=str(src),
        )
        if created:
            orig.save()

        simpleMeta = SimpleModelMeta.objects.create(
            originator=orig,
        )
        print(simpleMeta)
        if created:
            simpleMeta.save()
        simpleModel = SimpleModel.objects.create(
            meta =simpleMeta,
            model_id = model_id,
            idx = indx,
            field1 = field1,
            field2 = field2,
            field3 = field3,
            field4 = field4,
        )

        simpleModel.save()

    return HttpResponse("OK")