import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { GrupoProdutoProdutoDTO, GrupoCaracteristicaProdutoDTO, CaracteristicaProdutoDTO } from '../../../models/index';
import { RequestRepository } from '../../../repository/index';
import { AlertService } from '../../../services/index';
import {FormControl} from '@angular/forms';

@Component({
    selector: 'modal.caracteristicaPrepare.component',
    templateUrl: 'modal.caracteristicaPrepare.component.html',
})
export class ModalCaracteristicaPrepare {
    index: number;
    nomeProduto: string;
    caracteristicaProdutoDTOList: Array<CaracteristicaProdutoDTO>;
    grupoCaracteristicaProdutoDTOList: Array<GrupoCaracteristicaProdutoDTO>;
    dataModal: Array<GrupoProdutoProdutoDTO>;

    constructor(
        public requestRepository: RequestRepository,
        public alertService: AlertService,
        public dialogRef: MatDialogRef<ModalCaracteristicaPrepare>,
        @Inject(MAT_DIALOG_DATA) public data: Array<GrupoProdutoProdutoDTO>) {
        this.dataModal = [];
        for (let grupoProdutoProdutoDTO of data) {
            for (var i = 0; i < grupoProdutoProdutoDTO.quantidade; i++) {
                let dto = (JSON.parse(JSON.stringify(grupoProdutoProdutoDTO)));
                dto.quantidade = 1;
                for (let grupoCaracteristicaDTO of dto.grupoCaracteristicaProdutoDTOList) {
                    for (let caracteristicaDTO of grupoCaracteristicaDTO.caracteristicaProdutoDTOList) {
                        if (caracteristicaDTO.id == 7) { //Urgencia Normal
                            caracteristicaDTO.selected = true;
                        }
                    }
                }
                this.dataModal.push(dto);
            }
        }
        this.verifyAndFill();
    }

    verifyAndFill() {
        for (var i = 0; i < this.dataModal.length; i++) {
            var dto = this.dataModal[i];
            if (dto.grupoCaracteristicaProdutoDTOList.length > 0 && (dto.pendenteFillCaracteristicas == null || dto.pendenteFillCaracteristicas)) {
                this.index = i;
                this.grupoCaracteristicaProdutoDTOList = dto.grupoCaracteristicaProdutoDTOList;
                this.nomeProduto = dto.nome;
                this.caracteristicaProdutoDTOList = [];
                return;
            }
        }

        this.dialogRef.close(this.dataModal);
    }

    confirm(): void {
        this.dataModal[this.index].caracteristicaProdutoDTOList = this.caracteristicaProdutoDTOList;
        this.dataModal[this.index].pendenteFillCaracteristicas = false;
        this.verifyAndFill();
    }

    close(): void {
        this.dialogRef.close();
    }

    checkbox(evt: any, caracteristicaProdutoDTO: CaracteristicaProdutoDTO): void {
        if (evt.checked) {
            this.caracteristicaProdutoDTOList.push(caracteristicaProdutoDTO);
        } else {
            this.caracteristicaProdutoDTOList = this.caracteristicaProdutoDTOList.filter(c => c.id != caracteristicaProdutoDTO.id);
        }
    }

    radiobutton(evt: any, grupoCaracteristicaProdutoDTO: GrupoCaracteristicaProdutoDTO): void {
        this.caracteristicaProdutoDTOList = this.caracteristicaProdutoDTOList
            .filter(c => grupoCaracteristicaProdutoDTO.caracteristicaProdutoDTOList.filter(cf => cf.id == c.id).length == 0);
        this.caracteristicaProdutoDTOList.push(evt.value);
    }

}