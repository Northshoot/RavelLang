from django.db import models


class Originator(models.Model):
    device_id = models.CharField(max_length=255)
    address = models.CharField(max_length=255)


class SimpleModelMeta(models.Model):
    arrived = models.DateTimeField(auto_now_add=True)
    originator = models.ForeignKey("Originator")

    def __str__(self):
        return 'SimpleModelMeta from %s' %(self.originator.device_id)


# Create your models here.
class SimpleModel(models.Model):
    meta = models.ForeignKey("SimpleModelMeta")
    model_id = models.IntegerField(default =5)
    idx = models.IntegerField()
    field1 = models.IntegerField()
    field2 = models.IntegerField()
    field3 = models.IntegerField()
    field4 = models.IntegerField()

    def __str__(self):
        return 'SimpleModel: %s received %s' % (self.idx, self.meta.arrived.strftime("%B %d, %Y, %I:%M:%S %p") )